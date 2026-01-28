import type { Artist } from "./Artist";

export interface Album {
  id: number;
  title: string;
  artists: Artist[];
  covers: string[];
  createdAt: string;
}
