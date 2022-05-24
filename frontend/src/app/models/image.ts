export interface Image {
  readonly id: number;
  url: string;
  readonly addedOn: Date;
  readonly tags: Map<String, Number>;
  readonly width: Number;
  readonly height: Number;
}
