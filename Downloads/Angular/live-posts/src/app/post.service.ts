import { EventEmitter, Injectable } from '@angular/core';
import { Post } from './post/post.model';

@Injectable({ providedIn: 'root' })
export class PostService {

  listChangedEvent: EventEmitter<Post[]> = new EventEmitter()

  listOfPosts: Post[] = [
  ];

  getPosts() {
    return this.listOfPosts;
  }

  deletePost(index: number){
      this.listOfPosts.splice(index, 1)
  }

  addPost(post: Post){ 
      this.listOfPosts.push(post)
  }

  updatePost(index: number, post: Post){
      this.listOfPosts[index] = post
  }

  getPost(index: number){
    return this.listOfPosts[index]
  }

  likePost(index: number){
    this.listOfPosts[index].numberOfLikes += 1
  }

  setPosts(listOfPosts: Post[]){
    this.listOfPosts = listOfPosts;
    this.listChangedEvent.emit(listOfPosts);
  }

}
