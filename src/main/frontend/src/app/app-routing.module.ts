import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { HomeComponent } from './pages/home/home.component';
import { ResultsComponent } from './pages/results/results.component';
import { LoginComponent } from './pages/login/login.component';
import { RegisterComponent } from './pages/register/register.component';
import { ResultsHistoryComponent } from './pages/results-history/results-history.component';
import { TheoryComponent } from './pages/theory/theory.component';
import { MenuComponent } from './pages/menu/menu.component';
import { PasswordResetComponent } from './pages/password-reset/password-reset.component';



const routes: Routes = [
   {path:"", component:LoginComponent},
   {path:"home", component:HomeComponent},
   {path:"result", component:ResultsComponent},
   {path:"results-history", component:ResultsHistoryComponent},
   {path:"theory", component:TheoryComponent},
   {path:"register", component:RegisterComponent},
   {path:"menu", component:MenuComponent},
   {path:"password-reset", component:PasswordResetComponent}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
