import {LegalCaseDTO} from "./LegalCaseDTO";

export interface RulingTemplateLegalCaseDTO {
  legalCaseDTO: LegalCaseDTO;
  ruling: string;
  date?: string;
  judgementMeta?: string;
  explanationMeta?: string;
}
