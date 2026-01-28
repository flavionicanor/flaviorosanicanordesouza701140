package br.mt.artists.security.filter;

import io.github.bucket4j.*;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class RateLimitFilter extends OncePerRequestFilter {

    private static final int REQUESTS_PER_MINUTE = 40; // 10 edital

    private final Map<String, Bucket> buckets = new ConcurrentHashMap<>();

    private Bucket resolveBucket(String key) {
        return buckets.computeIfAbsent(key, k -> {
            Bandwidth limit = Bandwidth.classic(
                    REQUESTS_PER_MINUTE,
                    Refill.intervally(REQUESTS_PER_MINUTE, Duration.ofMinutes(1))
            );
            return Bucket.builder()
                    .addLimit(limit)
                    .build();
        });
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        String path = request.getRequestURI();

        // Rotas que NÃO devem consumir rate limit
        if (
                path.startsWith("/api/v1/auth") ||
                        path.startsWith("/swagger") ||
                        path.startsWith("/v3/api-docs") ||
                        path.startsWith("/actuator") ||
                        request.getMethod().equalsIgnoreCase("OPTIONS")
        ) {
            filterChain.doFilter(request, response);
            return;
        }

        // Chave do rate limit (JWT se existir, senão IP)
        String authHeader = request.getHeader("Authorization");
        String key = (authHeader != null && !authHeader.isBlank())
                ? authHeader
                : request.getRemoteAddr();

        Bucket bucket = resolveBucket(key);

        if (bucket.tryConsume(1)) {
            filterChain.doFilter(request, response);
        } else {
            response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
            response.setContentType("application/json");
            response.getWriter().write("""
                {
                  "status": 429,
                  "error": "Too Many Requests",
                  "message": "Limite de requisições excedido. Tente novamente em alguns instantes."
                }
                """);
        }
    }
}
