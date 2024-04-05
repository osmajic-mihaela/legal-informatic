import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {Observable} from 'rxjs';
import {LegalCase} from '../model/LegalCase';

@Injectable({
  providedIn: 'root'
})
export class CbrService {

  private apiHost = 'http://localhost:8080'
  headers: HttpHeaders = new HttpHeaders({ 'Content-Type': 'application/json' });
  constructor(private  http:HttpClient) { }

  getCaseReccomendation(caseDescription: LegalCase): Observable<LegalCase[]> {
    return this.http.post<LegalCase[]>(this.apiHost + `/case/recommend-case-solution`, caseDescription,{headers: this.headers});
  }

  getCaseVerdict(caseDescription: LegalCase): Observable<any> {
    return this.http.post<any>(this.apiHost + `/dr-device-cases/recommend-case-verdict`, caseDescription);
  }

}
