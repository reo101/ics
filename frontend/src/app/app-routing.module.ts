import { NgModule } from "@angular/core";
import { RouterModule, Routes } from "@angular/router";
import { AnalyzationComponent } from "./components/analyzation/analyzation.component";
import { GalleryComponent } from "./components/gallery/gallery.component";

const routes: Routes = [
  { path: "analyze", component: AnalyzationComponent },
  { path: "gallery", component: GalleryComponent },
  { path: "**", redirectTo: "analyze" },
]

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
