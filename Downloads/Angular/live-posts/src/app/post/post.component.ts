import { Component, Input, OnInit } from '@angular/core';
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

  constructor(private postService: PostService) { }

  //Al these methods would be called from components to activate it.
  ngOnInit(): void {
    console.log(this.post);
    console.log(this.index);
    
  }

  onDelete(){
    this.postService.deletePost(this.index)
  }

}
