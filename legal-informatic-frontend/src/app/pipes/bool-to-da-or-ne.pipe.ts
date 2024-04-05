import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'appBoolToDaOrNe',
})
export class BoolToDaOrNePipe implements PipeTransform {

  transform(value: boolean | string | undefined): string {
    if(value === undefined) return "NE";
    if (typeof value === 'string' || value as any instanceof String){
      if(value === 'true') return "DA";
      else return "NE";
    } else {
      if (value) return "DA";
      else "NE";
    }
    return "NE";
  }

}
