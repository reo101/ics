import { Component, OnInit } from "@angular/core";
import { MatDialog } from "@angular/material/dialog";
import { ActivatedRoute } from "@angular/router";
import { Image } from "src/app/models/image";
import { ImageService } from "src/app/services/image.service";
import { ImageDialogComponent } from "src/app/components/image-dialog/image-dialog.component";

@Component({
  selector: "app-gallery",
  templateUrl: "./gallery.component.html",
  styleUrls: ["./gallery.component.scss"]
})
export class GalleryComponent implements OnInit {

  public images?: Image[];
  public tags?: string[];

  ngOnInit(): void {
    this.route.params.subscribe(params => {
      this.tags = this.route.snapshot
        .paramMap
        .get("tags")
        ?.split(/,+/) ?? [];

      this.getImagesByTags(this.tags);
    });
  }

  constructor(
    private route: ActivatedRoute,
    private imageService: ImageService,
    private dialog: MatDialog,
  ) { }

  openDialog(image: Image) {
    const dialogRef = this.dialog.open(ImageDialogComponent, {
      maxWidth: "75vw",
      maxHeight: "80vh",
      data: image,
    })
  }

  getImages() {
    this.imageService.getImages().subscribe({
      next: (images: Image[]) => {
        this.images = images;
      },
      error: console.error,
    })
  }

  getImagesByTags(tags: string[]) {
    this.imageService.getImagesByTags(tags).subscribe({
      next: (images: Image[]) => {
        this.images = images;
      },
      error: console.error,
    });
  }

}
