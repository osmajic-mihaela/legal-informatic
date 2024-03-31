import { Component } from '@angular/core';
import { DomSanitizer, SafeHtml } from '@angular/platform-browser';
import { Router } from '@angular/router';
import { AkomaNtosoService } from 'src/app/service/AkomaNtosoService';

@Component({
  selector: 'app-akoma-ntoso-cases',
  templateUrl: './akoma-ntoso-cases.component.html',
  styleUrls: ['./akoma-ntoso-cases.component.scss']
})
export class AkomaNtosoCasesComponent {
  public xmlDocument: Document = new Document();
  public xmlHtml: SafeHtml | undefined;
  public judgements: String[] = [];
  public currentFile: String = '';
  public attributes: Map<String, String> = new Map();

  constructor(
    private akomaNtosoService: AkomaNtosoService,
    private sanitizer: DomSanitizer,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.akomaNtosoService.getCases().subscribe((data) => {
      this.judgements = data;
      this.currentFile = this.judgements[0];
      this.refreshCaseFile();
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
      console.log(response);
    });
  }

  openCaseFile(name: String) {
    this.currentFile = name;
    this.refreshCaseFile();
  }
}
