import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { HttpClientModule } from '@angular/common/http';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { MatDialogModule } from '@angular/material/dialog';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatButtonModule } from '@angular/material/button';
import { MatFormFieldModule } from '@angular/material/form-field';

// Modules
import { AppRoutingModule } from './app-routing.module';

// Components
import { RootComponent } from 'src/app/components/root/root.component';
import { AnalyzationComponent } from './components/analyzation/analyzation.component';
import { ImageDialogComponent } from './components/image-dialog/image-dialog.component';
import { GalleryComponent } from './components/gallery/gallery.component';
import { NavbarComponent } from './components/navbar/navbar.component';

// Pipes
import { SortByValuePipe } from './pipes/sortByValue.pipe';

@NgModule({
  declarations: [
    RootComponent,
    AnalyzationComponent,
    ImageDialogComponent,
    SortByValuePipe,
    GalleryComponent,
    NavbarComponent,
  ],
  imports: [
    BrowserModule,
    BrowserAnimationsModule,
    HttpClientModule,
    FormsModule,
    ReactiveFormsModule,
    AppRoutingModule,
    MatDialogModule,
    MatToolbarModule,
    MatButtonModule,
    MatFormFieldModule,
  ],
  providers: [],
  bootstrap: [RootComponent],
})
export class AppModule { }
