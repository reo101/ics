import { Component, Inject, LOCALE_ID, OnInit } from "@angular/core";
import { formatDate } from "@angular/common";
import { MatDialogRef, MAT_DIALOG_DATA } from "@angular/material/dialog";
import { Image } from "src/app/models/image";
import { SortByValuePipe } from "src/app/pipes/sortByValue.pipe";

@Component({
  selector: "app-image-popup",
  templateUrl: "./image-dialog.component.html",
  styleUrls: ["./image-dialog.component.scss"],
  providers: [SortByValuePipe],
})
export class ImageDialogComponent implements OnInit {

  public dateString: string;

  constructor(
    public dialogRef: MatDialogRef<ImageDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public image: Image,
    @Inject(LOCALE_ID) public locale: string,
  ) {
    this.dateString = formatDate(this.image.addedOn, "dd-MMM-YYYY HH:mm:ss", this.locale);
  }

  ngOnInit() {
  }

}
