export interface ILegalCase {
  id?: number;
  caseNumber?: string;
  court?: string;
  judge?: string;
  courtReporter?: string;
  defendant?: string;
  protectedSurface?: string;
  forestProperty?: string;
  financialDamage?: number;
  woodVolume?: number;
  awareness?: string;
  convicted?: string;
  numberOfTrees?: number;
  treeType?: string;
  reasonForProsecution?: string;
  conditionalSentence?: string;
  prisonSentence?: string;
  financialSentence?: number;
  communitySentence?: string;
  citedArticles?: string;
  deforestation?: boolean;
  desolation?: boolean;
  prohibited_land?: boolean;
  special_forest?: boolean;
  intention_to_sell?: boolean;
  had_intention?: boolean;
  date?: Date;
  judgementMeta?: string;
  explanationMeta?: string;
}

export class LegalCase implements ILegalCase {
  id?: number ;
  caseNumber?: string="";
  court?: string="";
  judge?: string="";
  courtReporter?: string="";
  defendant?: string="";
  protectedSurface?: string="";
  forestProperty?: string="";
  financialDamage?: number=0;
  woodVolume?: number=0;
  awareness?: string="";
  convicted?: string="";
  numberOfTrees?: number=0;
  treeType?: string="";
  reasonForProsecution?: string="";
  conditionalSentence?: string="";
  prisonSentence?: string="0";
  financialSentence?: number=0;
  communitySentence?: string="0";
  citedArticles?: string="";
  deforestation?: boolean;
  desolation?: boolean;
  prohibited_land?: boolean;
  special_forest?: boolean;
  intention_to_sell?: boolean;
  had_intention?: boolean;
  date?: Date;
  judgementMeta?: string;
  explanationMeta?: string;
  constructor(data?: ILegalCase) {
    if (data) {
      for (const property in data) {
        if (data.hasOwnProperty(property))
          (<any>this)[property] = (<any>data)[property];
      }
    }

  }
}
