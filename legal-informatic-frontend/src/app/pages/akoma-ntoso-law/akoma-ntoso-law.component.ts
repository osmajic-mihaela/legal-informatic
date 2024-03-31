import { Component } from '@angular/core';
import { DomSanitizer, SafeHtml } from '@angular/platform-browser';
import { Router } from '@angular/router';
import { AkomaNtosoService } from 'src/app/service/AkomaNtosoService';

@Component({
  selector: 'app-akoma-ntoso-law',
  templateUrl: './akoma-ntoso-law.component.html',
  styleUrls: ['./akoma-ntoso-law.component.scss']
})
export class AkomaNtosoLawComponent{
  public xmlDocument: Document = new Document();
  public xmlHtml: SafeHtml | undefined;

  constructor(
    private akomaNtosoService: AkomaNtosoService,
    private sanitizer: DomSanitizer,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.akomaNtosoService.getLaw().subscribe((response: any) => {
      this.xmlDocument = new DOMParser().parseFromString(response, 'text/xml');
      this.xmlHtml = this.sanitizer.bypassSecurityTrustHtml(
        new XMLSerializer().serializeToString(this.xmlDocument)
      );

      window.onload = () => {
        let scrollTo = this.router.url.split('#')[1];
        if (scrollTo != undefined) {
          const myElement = document.getElementById(scrollTo);
          if (myElement != null) {
            myElement.scrollIntoView();
          }
        }
      };
    });
  }
}
