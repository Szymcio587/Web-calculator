import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import {MatButtonModule} from '@angular/material/button';
import {MatDialogModule} from '@angular/material/dialog';
import {FormsModule} from '@angular/forms';
import {MatNativeDateModule} from '@angular/material/core';
import {HttpClientModule} from '@angular/common/http';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { NavbarComponent } from './shared/navbar/navbar.component';
import { FooterComponent } from './shared/footer/footer.component';
import { HomeComponent} from './pages/home/home.component';
import { AuthorComponent } from './pages/author/author.component';
import { MethodsComponent } from './pages/methods/methods.component';
import { ResultsComponent } from './pages/results/results.component';
import { ContactComponent } from './pages/contact/contact.component';
import { InterpolationComponent } from './pages/home/dialog/interpolation/interpolation.component';
import { AproximationComponent } from './pages/home/dialog/aproximation/aproximation.component';
import { IntegralComponent } from './pages/home/dialog/integral/integral.component';
import { GaussComponent } from './pages/home/dialog/gauss/gauss.component';
import { ErrorHandler } from '@angular/core';
import { MainErrorHandler } from './error.handler';


@NgModule({
  declarations: [
    AppComponent,
    NavbarComponent,
    FooterComponent,
    HomeComponent,
    AuthorComponent,
    MethodsComponent,
    ResultsComponent,
    ContactComponent,
    InterpolationComponent,
    AproximationComponent,
    IntegralComponent,
    GaussComponent
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
