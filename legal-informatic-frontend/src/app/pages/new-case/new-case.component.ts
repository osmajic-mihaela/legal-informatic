import { Component, OnInit } from '@angular/core';
import {LegalCase} from "../../model/LegalCase";
import {CbrService} from "../../service/CbrService";
import {LegalCaseService} from "../../service/LegalCaseService";

@Component({
  selector: 'app-new-case',
  templateUrl: './new-case.component.html',
  styleUrls: ['./new-case.component.scss']
})
export class NewCaseComponent implements OnInit {
  public isLoading = false;
  public legalCase: LegalCase = new LegalCase();
  public caseRecommendation :any[] = [];
  selectedOffense: string;
  stav : string;




  constructor(private cbrService: CbrService, private  legalCaseService:LegalCaseService) {
    this.legalCase = new LegalCase();
    this.selectedOffense = "";
    this.stav = "";
  }

  ngOnInit(): void {
  }

  getRecommendations() {
    this.isLoading = true;
    this.cbrService.getCaseReccomendation(this.legalCase).subscribe((response) => {
      this.caseRecommendation = response;
      this.isLoading = false;
    });
  }

  addNewCase() {
    this.legalCaseService.addNewCase(this.legalCase).subscribe((response) => {
      console.log(response)
      alert('Slučaj ' + response.caseNumber+' je zabeležen u bazi!');
      this.isLoading = false;
    });
  }

  updateReason() {
    this.legalCase.reasonForProsecution = 'čl. '+this.selectedOffense+' st. '+this.stav;

  }

  updateStav() {
    this.legalCase.reasonForProsecution = 'čl. '+this.selectedOffense+' st. '+this.stav;

  }

  isValidForRecommendation() {
    console.log(this.legalCase)
    return (
      this.legalCase.caseNumber=='' ||
      this.legalCase.court=='' ||
      this.legalCase.judge=='' ||
      this.legalCase.courtReporter=='' ||
      this.legalCase.defendant=='' ||
      this.legalCase.protectedSurface=='' ||
      this.legalCase.forestProperty=='' ||
      this.legalCase.financialDamage ==0 ||
      this.legalCase.woodVolume ==0 ||
      this.legalCase.awareness=='' ||
      this.legalCase.numberOfTrees==0  ||
      this.legalCase.treeType=='' ||
      this.legalCase.reasonForProsecution==''
    );
  }


  isValidForNewCaseAdding() {
    return (
      this.legalCase.caseNumber=='' ||
      this.legalCase.court=='' ||
      this.legalCase.judge=='' ||
      this.legalCase.courtReporter=='' ||
      this.legalCase.defendant=='' ||
      this.legalCase.protectedSurface=='' ||
      this.legalCase.forestProperty=='' ||
      this.legalCase.financialDamage ==0 ||
      this.legalCase.woodVolume ==0 ||
      this.legalCase.awareness=='' ||
      this.legalCase.numberOfTrees==0  ||
      this.legalCase.treeType=='' ||
      this.legalCase.reasonForProsecution=='' ||
      this.legalCase.convicted=='' ||
      this.legalCase.citedArticles==''

    );
  }
}
