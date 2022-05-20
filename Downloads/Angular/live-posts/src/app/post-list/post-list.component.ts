import { Component, OnInit } from '@angular/core';
import { PostService } from '../post.service';
import { Post } from '../post/post.model';

@Component({
  selector: 'app-post-list',
  templateUrl: './post-list.component.html',
  styleUrls: ['./post-list.component.css'],
})
export class PostListComponent implements OnInit {
  //Atributtes.
  listOfPosts: Post[] = []
  
  //Construcctor recives dependencies and more.
  constructor(private postService: PostService) {}

  //Method when it inits.
  ngOnInit(): void {
    this.listOfPosts = this.postService.getPosts()
    this.postService.listChangedEvent.subscribe((listOfPosts:Post[]) => {
      this.listOfPosts = this.postService.getPosts()
    })
  }

}
