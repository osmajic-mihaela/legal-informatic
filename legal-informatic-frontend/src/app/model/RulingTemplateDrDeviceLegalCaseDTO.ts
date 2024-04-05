import {DrDeviceLegalCaseDTO} from "./DrDeviceLegalCaseDTO";

export interface RulingTemplateDrDeviceLegalCaseDTO{
  drDeviceLegalCaseDTO: DrDeviceLegalCaseDTO;
  ruling: string;
  date?: string;
  judgementMeta?: string;
  explanationMeta?: string;
}
