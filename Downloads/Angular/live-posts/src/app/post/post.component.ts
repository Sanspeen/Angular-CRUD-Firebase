import { Component, Input, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { PostService } from '../post.service';
import { Post } from './post.model';

@Component({
  selector: 'app-post',
  templateUrl: './post.component.html',
  styleUrls: ['./post.component.css']
})
export class PostComponent implements OnInit {
  @Input() post?: Post;
  @Input() index: number = 0;

  constructor(private postService: PostService, private router: Router) { }

  //Al these methods would be called from components to activate it.
  ngOnInit(): void {
    
  }

  onDelete(){
    this.postService.deletePost(this.index)
  }

  onEdit(){
    this.router.navigate(["/post-edit", this.index])    
  }

}
