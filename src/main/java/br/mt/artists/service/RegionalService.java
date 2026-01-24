package br.mt.artists.service;

import br.mt.artists.domain.dto.external.RegionalExternalDTO;
import br.mt.artists.domain.entity.Regional;
import br.mt.artists.repository.RegionalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RegionalService {

    private final RegionalRepository repository;
    private final RestTemplate restTemplate;

    private static final String URL = "https://integrador-argus-api.geia.vip/v1/regionais";

    @Transactional
    public void syncRegionais() {
        RegionalExternalDTO[] externos = restTemplate.getForObject(URL, RegionalExternalDTO[].class);

        Map<Integer, RegionalExternalDTO> externosMap = Arrays.stream(externos).collect(
                Collectors.toMap(RegionalExternalDTO::getId, Function.identity()
        ));

        // Inativar Ausentes
        repository.findAllByAtivoTrue().forEach(regional -> {
            if(!externosMap.containsKey(regional.getId())){
                regional.setAtivo(false);
            }
        });

        // Inserir ou Versionar
        externosMap.values().forEach(dto -> {
            Optional<Regional> atual = repository.findTopByIdAndAtivoTrueOrderByCreatedAtDesc(dto.getId());

            if(atual.isEmpty()){
                inserirDTO(dto);
            } else if (!atual.get().getNome().equals(dto.getNome())) {
                atual.get().setAtivo(false);
                inserirDTO(dto);
            }
        });
    }

    private void inserirDTO(RegionalExternalDTO dto) {
        Regional reg = new Regional();
        reg.setId(dto.getId());
        reg.setNome(dto.getNome());
        reg.setAtivo(true);
        reg.setCreatedAt(LocalDateTime.now());
        repository.save(reg);
    }

    @Transactional(readOnly = true)
    public List<Regional> listAtivos() {
        return repository.findAllByAtivoTrue();
    }

}
