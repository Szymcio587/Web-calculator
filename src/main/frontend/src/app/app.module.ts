import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import {MatButtonModule} from '@angular/material/button';
import {MatDialogModule} from '@angular/material/dialog';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {MatNativeDateModule} from '@angular/material/core';
import {HttpClientModule} from '@angular/common/http';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { NavbarComponent } from './shared/navbar/navbar.component';
import { FooterComponent } from './shared/footer/footer.component';
import { HomeComponent} from './pages/home/home.component';
import { ResultsComponent } from './pages/results/results.component';
import { InterpolationComponent } from './pages/home/dialog/interpolation/interpolation.component';
import { IntegralComponent } from './pages/home/dialog/integral/integral.component';
import { ErrorHandler } from '@angular/core';
import { MainErrorHandler } from './error.handler';
import { NgxEchartsModule } from 'ngx-echarts';
import { SystemOfEquationsComponent } from './pages/home/dialog/system-of-equations/system-of-equations.component';
import { LoginComponent } from './pages/login/login.component';
import { RegisterComponent } from './pages/register/register.component';
import { InterpolationOptionsDialogComponent } from './pages/home/dialog/interpolation-options-dialog/interpolation-options-dialog.component';
import { IntegrationOptionsDialogComponent } from './pages/home/dialog/integration-options-dialog/integration-options-dialog.component';
import { ResultsHistoryComponent } from './pages/results-history/results-history.component';
import { TheoryComponent } from './pages/theory/theory.component';
import { MenuComponent } from './pages/menu/menu.component';
import { PasswordResetComponent } from './pages/password-reset/password-reset.component';


@NgModule({
  declarations: [
    AppComponent,
    NavbarComponent,
    FooterComponent,
    HomeComponent,
    ResultsComponent,
    TheoryComponent,
    ResultsHistoryComponent,
    InterpolationComponent,
    IntegralComponent,
    SystemOfEquationsComponent,
    LoginComponent,
    RegisterComponent,
    InterpolationOptionsDialogComponent,
    IntegrationOptionsDialogComponent,
    ResultsHistoryComponent,
    TheoryComponent,
    MenuComponent,
    PasswordResetComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    FormsModule,
    MatButtonModule,
    MatDialogModule,
    BrowserAnimationsModule,
    HttpClientModule,
    MatNativeDateModule,
    ReactiveFormsModule,
    NgxEchartsModule.forRoot({
      echarts: () => import('echarts'),
    }),
  ],
  exports: [
    MatButtonModule,
    MatDialogModule
  ],
  providers: [
    { provide: ErrorHandler, useClass: MainErrorHandler },
  ],
  bootstrap: [AppComponent, HomeComponent]
})
export class AppModule { }
