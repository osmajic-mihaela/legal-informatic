import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {Observable} from "rxjs";
import {LegalCase} from "../model/LegalCase";

@Injectable({
  providedIn: 'root'
})
export class AkomaNtosoService {

  private apiHost = 'http://localhost:8080'
  constructor(private  http:HttpClient) { }

  getLaw() {
    return this.http.get<any>(this.apiHost + `/law/krivicni`, {  responseType: 'text' as 'json' });
  }

  getCases() {
    return this.http.get<any>(this.apiHost + `/case/cases-akoma-ntoso`);
  }

  getCase(name: String) {
    return this.http.get<any>(this.apiHost + `/case/cases-akoma-ntoso/`+ name, {
      responseType: 'text' as 'json',
    });
  }

  getCaseAttributes(name: String) {
    return this.http.get<any>(this.apiHost + `/case/preview-case-attributes/`+ name);
  }
  
  
}
