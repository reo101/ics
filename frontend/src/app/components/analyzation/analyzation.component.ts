import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { MatDialog } from '@angular/material/dialog';
// import { MatDialog } from '@angular/material/dialog';
import { Image } from 'src/app/models/image';
import { ImageService } from 'src/app/services/image.service';
import { ImageDialogComponent } from '../image-dialog/image-dialog.component';

@Component({
  selector: 'app-analyzation',
  templateUrl: './analyzation.component.html',
  styleUrls: ['./analyzation.component.scss']
})
export class AnalyzationComponent implements OnInit {

  form: FormGroup = new FormGroup({
    url: new FormControl(''),
  });
  private image?: Image;
  private urlPattern: RegExp =
    /^https?:\/\/(?:[a-z0-9\-]+\.)+[a-z]{2,6}(?:\/[^\/#?]+)+\.(?:jpe?g|gif|png|bmp)?(\?.*)$/i;

  ngOnInit() {
  }

  constructor(
    private imageService: ImageService,
    private formBuilder: FormBuilder,
    private dialog: MatDialog,
  ) {
    this.form = this.formBuilder.group({
      url: ['', [Validators.required, Validators.pattern(this.urlPattern)]]
    });
  }

  isInvalid(pristine: boolean) {
    return (pristine && this.form.controls["url"].pristine) ||
      (this.form.controls["url"].touched && this.form.controls["url"].invalid);
  }

  hasError(error: string) {
    return this.form.controls["url"].errors && this.form.controls["url"].errors[error];
  }

  openDialog() {
    const dialogRef = this.dialog.open(ImageDialogComponent, {
      maxWidth: "75vw",
      maxHeight: "80vh",
      data: this.image,
    });
  }

  addImage(image: { url: Image["url"] }) {
    this.imageService.addImage(image as Image).subscribe({
      next: (image: Image) => {
        this.image = image;
        this.openDialog();
      },
      error: console.error,
    });
  }

}
