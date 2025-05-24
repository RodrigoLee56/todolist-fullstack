import { Component, OnInit, Input } from '@angular/core';
import { Router } from '@angular/router';
import { TaskService } from '../../services/task.service';
import { Task } from '../../models/task';

@Component({
  selector: 'app-task-list',
  templateUrl: './task-list.component.html',
  styleUrls: ['./task-list.component.css']
})
export class TaskListComponent implements OnInit {
  @Input() tasks: any[] = [];
  loading: boolean = false;
  error: string = '';

  constructor(private taskService: TaskService, private router: Router) {}

  ngOnInit(): void {
    this.getTasks();
  }

  getTasks(): void {
    this.loading = true;
    interface GetAllResponse {
      // Adjust this interface if your API returns a different structure
      // For now, assuming it returns Task[]
      // tasks: Task[];
    }

    this.taskService.getTasks().subscribe({
      next: (tasks: Task[]) => {
      this.tasks = tasks;
      this.loading = false;
      },
      error: (err: any) => {
      this.error = 'Erro ao carregar tarefas';
      this.loading = false;
      }
    });
  }

  editTask(task: Task): void {
    this.router.navigate(['/tasks', task.id, 'edit']);
  }

  deleteTask(task: Task): void {
    if (task.id === undefined) {
      this.error = 'ID da tarefa não definido';
      return;
    }
    if (confirm('Tem certeza que deseja excluir esta tarefa?')) {
      this.taskService.deleteTask(task.id).subscribe({
        next: () => this.getTasks(),
        error: () => this.error = 'Erro ao excluir tarefa'
      });
    }
  }

  createTask(): void {
    this.router.navigate(['/tasks/new']);
  }
}
