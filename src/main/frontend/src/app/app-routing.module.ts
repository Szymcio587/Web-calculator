import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { HomeComponent } from './pages/home/home.component';
import { AuthorComponent } from './pages/author/author.component';
import { MethodsComponent } from './pages/methods/methods.component';
import { ResultsComponent } from './pages/results/results.component';
import { ContactComponent } from './pages/contact/contact.component';
import { LoginComponent } from './pages/login/login.component';
import { RegisterComponent } from './pages/register/register.component';



const routes: Routes = [
   {path:"", component:LoginComponent},
   {path:"home", component:HomeComponent},
   {path:"author", component:AuthorComponent},
   {path:"methods", component:MethodsComponent},
   {path:"result", component:ResultsComponent},
   {path:"contact", component:ContactComponent},
   {path:"register", component:RegisterComponent}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
