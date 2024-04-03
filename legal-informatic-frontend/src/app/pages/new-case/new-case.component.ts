import {Component, OnInit, ViewEncapsulation} from '@angular/core';
import {LegalCase} from "../../model/LegalCase";
import {CbrService} from "../../service/CbrService";
import {LegalCaseService} from "../../service/LegalCaseService";
import {DomSanitizer, SafeHtml} from "@angular/platform-browser";

@Component({
  selector: 'app-new-case',
  templateUrl: './new-case.component.html',
  styleUrls: ['./new-case.component.scss'],
})
export class NewCaseComponent implements OnInit {
  public isLoadingVerdictRecommendation = false;
  public isLoadingCaseRecommendation = false;
  public legalCase: LegalCase = new LegalCase();
  public caseRecommendation :any[] = [];
  public verdictRecommendation :any[] = [];
  selectedOffense: string;
  stav: string;
  caseGeneratedByCases: SafeHtml | undefined;
  caseGeneratedByRules: SafeHtml | undefined;


  constructor(private cbrService: CbrService,
              private  legalCaseService: LegalCaseService,
              private sanitizer: DomSanitizer) {
    this.legalCase = new LegalCase();
    this.selectedOffense = "";
    this.stav = "";
  }

  ngOnInit(): void {
  }

  getRecommendations() {
    this.isLoadingCaseRecommendation = true;
    this.isLoadingVerdictRecommendation = true;

    this.cbrService.getCaseReccomendation(this.legalCase).subscribe((response) => {
      this.caseRecommendation = response;
      this.isLoadingCaseRecommendation = false;
    });
    const dto = {
      ...this.legalCase,
      prohibited_land: this.legalCase.protectedSurface === 'true'
    }
    this.cbrService.recommendCaseVerdict(dto).subscribe((response) => {
      this.verdictRecommendation = response;
      this.isLoadingVerdictRecommendation = false;
    });
  }

  addNewCase() {
    this.legalCaseService.addNewCase(this.legalCase).subscribe((response) => {
      alert('Slučaj ' + response.caseNumber+' je zabeležen u bazi!');
    });
    let caseRecommendations = '';
    this.caseRecommendation.forEach((recommendation) => {
      caseRecommendations += '<br /><br />' + recommendation;
    });
    this.legalCaseService.getTemplateByCases({
      ruling: caseRecommendations,
      legalCaseDTO: {
        caseNumber: this.legalCase.caseNumber ?? '',
        court: this.legalCase.court ?? '',
        awareness: this.legalCase.awareness ?? 'false',
        citedArticles: this.legalCase.citedArticles ?? '',
        communitySentence: this.legalCase.communitySentence ?? '',
        conditionalSentence: this.legalCase.conditionalSentence ?? '',
        convicted: this.legalCase.convicted ?? 'false',
        courtReporter: this.legalCase.courtReporter ?? '',
        defendant: this.legalCase.defendant ?? '',
        financialDamage: this.legalCase.financialDamage?.toString() ?? '',
        judge: this.legalCase.judge ?? '',
        financialSentence: this.legalCase.financialSentence?.toString() ?? '',
        forestProperty: this.legalCase.forestProperty ?? '',
        numberOfTrees: this.legalCase.numberOfTrees?.toString() ?? '',
        protectedSurface: this.legalCase.protectedSurface ?? 'true',
        treeType: this.legalCase.treeType ?? '',
        woodVolume: this.legalCase.woodVolume?.toString() ?? '',
        reasonForProsecution: this.legalCase.reasonForProsecution ?? '',
        prisonSentence: this.legalCase.prisonSentence ?? ''
      }
    }).subscribe((response) => {
      let xmlDocument = new DOMParser().parseFromString(
        response,
        'text/xml'
      );
      this.caseGeneratedByCases = this.sanitizer.bypassSecurityTrustHtml(
        new XMLSerializer().serializeToString(xmlDocument)
      );
    });
    let ruleRecommendations = '';
    this.verdictRecommendation.forEach((recommendation) => {
      ruleRecommendations += '<br /><br />' + recommendation;
    });
    this.legalCaseService.getTemplateByRules({
      ruling: ruleRecommendations,
      drDeviceLegalCaseDTO: {
        caseNumber: this.legalCase.caseNumber ?? '',
        court: this.legalCase.court ?? '',
        judge: this.legalCase.judge ?? '',
        courtReporter: this.legalCase.courtReporter ?? '',
        defendant: this.legalCase.defendant ?? '',
        deforestation: this.legalCase.deforestation ?? false,
        desolation: this.legalCase.desolation ?? false,
        prohibited_land: this.legalCase.protectedSurface === 'true',
        special_forest: this.legalCase.special_forest ?? false,
        woodVolume: this.legalCase.woodVolume ?? 0,
        intention_to_sell: this.legalCase.intention_to_sell ?? false,
        had_intention: this.legalCase.had_intention ?? false
      }
    }).subscribe((response) => {
      let xmlDocument = new DOMParser().parseFromString(
        response,
        'text/xml'
      );
      this.caseGeneratedByRules = this.sanitizer.bypassSecurityTrustHtml(
        new XMLSerializer().serializeToString(xmlDocument)
      );
    });
  }

  updateReason() {
    this.legalCase.reasonForProsecution = 'čl. '+this.selectedOffense+' st. '+this.stav;

  }

  updateStav() {
    this.legalCase.reasonForProsecution = 'čl. '+this.selectedOffense+' st. '+this.stav;

  }

  isValidForRecommendation() {
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
      this.legalCase.deforestation === undefined ||
      this.legalCase.desolation === undefined ||
      this.legalCase.special_forest === undefined ||
      this.legalCase.had_intention === undefined ||
      this.legalCase.intention_to_sell === undefined
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
      this.legalCase.convicted == '' ||
      this.legalCase.citedArticles=='' ||
      this.legalCase.deforestation === undefined ||
      this.legalCase.desolation === undefined ||
      this.legalCase.special_forest === undefined ||
      this.legalCase.had_intention === undefined ||
      this.legalCase.intention_to_sell === undefined

    );
  }
}
