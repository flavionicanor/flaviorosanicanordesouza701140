import { api } from "./api.ts";

export type Artist = {
  id: number;
  name: string;
  albumsCount?: number;
};

export type ArtistsResponse = {
  content: Artist[];
  totalPages: number;
};

export function getArtists(name: string, page: number, sort: "asc" | "desc") {
  return api.get<ArtistsResponse>("/artists", {
    params: {
      name,
      page,
      size: 10,
      sorte: `name,${sort}`,
    },
  });
}
