import { Component } from '@angular/core';
import { Input } from '@angular/core';
import { Task } from '../../models/task';


@Component({
  selector: 'app-task-list',
  templateUrl: './task-list.component.html',
  styleUrl: './task-list.component.css'
})
export class TaskListComponent {
  @Input() tasks: Task[] = [];
}
