import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {Observable} from "rxjs";
import {LegalCase} from "../model/LegalCase";
import {RulingTemplateLegalCaseDTO} from "../model/RulingTemplateLegalCaseDTO";
import {RulingTemplateDrDeviceLegalCaseDTO} from "../model/RulingTemplateDrDeviceLegalCaseDTO";

@Injectable({
  providedIn: 'root'
})
export class LegalCaseService {

  private apiHost = 'http://localhost:8080'
  headers: HttpHeaders = new HttpHeaders({ 'Content-Type': 'application/json' });
  constructor(private  http: HttpClient) { }

  addNewCase(caseDescription: LegalCase): Observable<LegalCase> {
    return this.http.post<LegalCase>(this.apiHost + `/case/add-new-case`, caseDescription, { headers: this.headers });
  }

  getTemplateByCases(legalCase: RulingTemplateLegalCaseDTO): Observable<any>{
    return this.http.post<any>(this.apiHost + `/ruling-template/template-legal-case`, legalCase, {  responseType: 'text' as 'json' });
  }
  getTemplateByRules(legalCase: RulingTemplateDrDeviceLegalCaseDTO): Observable<any>{
    return this.http.post<any>(this.apiHost + `/ruling-template/template-dr-device-legal-case`,
      legalCase, {  responseType: 'text' as 'json' });
  }

  importData(): Observable<any>{
    return this.http.get<any>(this.apiHost + `/case/import`, {  responseType: 'text' as 'json' });
  }
}
