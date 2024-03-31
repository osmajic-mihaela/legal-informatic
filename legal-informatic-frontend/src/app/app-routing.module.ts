import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { NotFoundComponent } from './pages/not-found/not-found.component';
import {NewCaseComponent} from "./pages/new-case/new-case.component";
import { AkomaNtosoLawComponent } from './pages/akoma-ntoso-law/akoma-ntoso-law.component';
import { AkomaNtosoCasesComponent } from './pages/akoma-ntoso-cases/akoma-ntoso-cases.component';

const routes: Routes = [
  {
    path: '',
    pathMatch: 'full',
    redirectTo: 'home',
  },
  {
    path: 'new-case',
    component: NewCaseComponent,
  },
  {
    path: 'krivicni',
    component: AkomaNtosoLawComponent,
  },
  {
    path: 'krivicni/**',
    component: AkomaNtosoLawComponent,
  },
  {
    path: 'akoma-ntoso-cases',
    component: AkomaNtosoCasesComponent,
  },
  {
    path: '**',
    component: NotFoundComponent,
  },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})
export class AppRoutingModule {}
