import { HttpClient } from '@angular/common/http';
import { Component, Inject, OnInit } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { Router } from '@angular/router';
import { SYSTEM_OF_EQUATIONS_DATA } from 'src/app/shared/data/data.constants';
import { SystemOfEquationsData, SystemOfEquationsResponse } from 'src/app/shared/data/data.interface';
import { ResultDataService } from 'src/app/shared/services/result/result-data.service';
import { UserService } from 'src/app/shared/services/user/user.service';

@Component({
  selector: 'app-system-of-equations',
  templateUrl: './system-of-equations.component.html',
  styleUrls: ['./system-of-equations.component.css']
})
export class SystemOfEquationsComponent implements OnInit {

  data: SystemOfEquationsData = {
    dataType: '',
    username: '',
    coefficients: [[0, 0], [0, 0]],
    constants: [0, 0]
  };

  constructor(private http: HttpClient, private router: Router, public dialogRef: MatDialogRef<SystemOfEquationsComponent>) {
  }

  ngOnInit(): void {
  }

  Submit() {
    const data: SystemOfEquationsData = {
      dataType: SYSTEM_OF_EQUATIONS_DATA,
      username: UserService.getUsername(),
      coefficients: this.data.coefficients,
      constants: this.data.constants
    };
    this.http.post<SystemOfEquationsResponse>('http://localhost:8081/calculations/system_of_equations', data).subscribe(
      (result: SystemOfEquationsResponse) => {
        ResultDataService.SetSystemOfEquationsResult(result.solutions, data)
        ResultDataService.SetResultType(result.name);
        this.router.navigate(['/result']);
      },
      (error: any) => {
        console.error("Error:", error);
      }
    );
    this.dialogRef.close();
  }
}
