import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatSidenavModule } from '@angular/material/sidenav';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatDividerModule } from '@angular/material/divider';
import { HomeComponent } from './pages/home/home.component';
import { NotFoundComponent } from './pages/not-found/not-found.component';
import { NewCaseComponent } from './pages/new-case/new-case.component';
import {MatFormFieldModule} from "@angular/material/form-field";
import {MatSelectModule} from "@angular/material/select";
import {MatOptionModule} from "@angular/material/core";
import {MatProgressSpinnerModule} from "@angular/material/progress-spinner";
import {MatInputModule} from "@angular/material/input";
import { FormsModule } from '@angular/forms';
import {HttpClientModule} from "@angular/common/http";
import { AkomaNtosoCasesComponent } from './pages/akoma-ntoso-cases/akoma-ntoso-cases.component';
import { AkomaNtosoLawComponent } from './pages/akoma-ntoso-law/akoma-ntoso-law.component';
import { MatListModule } from '@angular/material/list';
import { BoolToDaOrNePipe } from './pipes/bool-to-da-or-ne.pipe';
import {MatCheckboxModule} from "@angular/material/checkbox";

@NgModule({
  declarations: [AppComponent, HomeComponent, NotFoundComponent, NewCaseComponent, AkomaNtosoCasesComponent, AkomaNtosoLawComponent, BoolToDaOrNePipe],
    imports: [
        BrowserModule,
        AppRoutingModule,
        BrowserAnimationsModule,
        MatToolbarModule,
        MatSidenavModule,
        MatButtonModule,
        MatIconModule,
        MatDividerModule,
        MatFormFieldModule,
        MatSelectModule,
        MatOptionModule,
        MatProgressSpinnerModule,
        MatInputModule,
        FormsModule,
        HttpClientModule,
        MatListModule,
        MatCheckboxModule
    ],
  providers: [],
  bootstrap: [AppComponent],
})
export class AppModule {}
