import { Component } from '@angular/core';
import { DomSanitizer, SafeHtml } from '@angular/platform-browser';
import { Router } from '@angular/router';
import { BehaviorSubject } from 'rxjs';
import { ILegalCase } from 'src/app/model/LegalCase';
import { AkomaNtosoService } from 'src/app/service/AkomaNtosoService';
import {LegalCaseService} from "../../service/LegalCaseService";

@Component({
  selector: 'app-akoma-ntoso-cases',
  templateUrl: './akoma-ntoso-cases.component.html',
  styleUrls: ['./akoma-ntoso-cases.component.scss']
})
export class AkomaNtosoCasesComponent {
  public xmlDocument: Document = new Document();
  public xmlHtml: SafeHtml | undefined;
  public cases: String[] = [];
  public currentFile: String = '';
  public caseMetadata$ = new BehaviorSubject<ILegalCase|undefined>(undefined);

  constructor(
    private akomaNtosoService: AkomaNtosoService,
    private sanitizer: DomSanitizer,
    private  legalCaseService: LegalCaseService
  ) {}

  ngOnInit(): void {
    this.legalCaseService.importData().subscribe((response) => {
      console.log(response)
      this.akomaNtosoService.getCases().subscribe((data) => {
        this.cases = data;
        this.currentFile = this.cases[0];
        this.refreshCaseFile();
      });
    });

  }

  refreshCaseFile() {
    this.akomaNtosoService
      .getCase(this.currentFile)
      .subscribe((response: any) => {
        this.xmlDocument = new DOMParser().parseFromString(
          response,
          'text/xml'
        );
        this.xmlHtml = this.sanitizer.bypassSecurityTrustHtml(
          new XMLSerializer().serializeToString(this.xmlDocument)
        );
      });
    this.akomaNtosoService.getCaseAttributes(this.currentFile.split('.')[0]).subscribe((response: any) => {
      if (response?.communitySentence !== undefined){
        if (response.communitySentence == "0"){
          response.communitySentence = "-";
        }
      }
      this.caseMetadata$.next(response);
    });
  }

  openCaseFile(name: String) {
    this.currentFile = name;
    this.refreshCaseFile();
  }
}
