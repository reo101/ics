import { Injectable } from "@angular/core";
import { HttpClient, HttpHeaders } from "@angular/common/http";
import { Image } from "../models/image";
import { Observable } from "rxjs";
import { environment } from "src/environments/environment";

@Injectable({
  providedIn: "root",
})
export class ImageService {
  private httpOptions = {
    headers: (() => {
      var headers = new HttpHeaders();

      headers.append("Access-Control-Allow-Origin", "*");
      headers.append("Content-Type", "application/json");

      return headers;
    })()
  };

  constructor(private http: HttpClient) { }

  public getImages(): Observable<Image[]> {
    return this.http.get<Image[]>(
      `${environment.backendUrl}/images`,
      this.httpOptions);
  }

  public getImageById(id: number): Observable<Image> {
    return this.http.get<Image>(
      `${environment.backendUrl}/images/${id}`,
      this.httpOptions);
  }

  public getImagesByTags(tags: string[]): Observable<Image[]> {
    return this.http.get<Image[]>(
      `${environment.backendUrl}/images?tags=${tags.join(",")}`,
      this.httpOptions);
  }

  public addImage(image: Image, noCache: Boolean = false): Observable<Image> {
    return this.http.post<Image>(
      `${environment.backendUrl}/images/add?noCache=${noCache}`,
      image,
      this.httpOptions);
  }

  // public deleteImage(image: Image): Observable<Image> {
  //   return this.http.delete(`${environment.backendUrl}/images/delete`, image);
  // }
}
