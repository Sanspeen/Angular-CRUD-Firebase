import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { tap } from 'rxjs';
import { PostService } from './post.service';
import { Post } from './post/post.model';

const DATABASE = 'https://live-posts-73174-default-rtdb.firebaseio.com/';

@Injectable({ providedIn: 'root' })
export class BackEndService {
  constructor(private http: HttpClient, private postService: PostService) {}

  saveData() {
    const listOfPosts: Post[] = this.postService.getPosts();

    this.http.put(DATABASE + 'posts.json', listOfPosts).subscribe((res) => {
        console.log(res);
    })
  }

  fetchData(){
      this.http.get<Post[]>('https://live-posts-73174-default-rtdb.firebaseio.com/posts.json')
      .pipe(tap((listOfPosts: Post[]) => {
          this.postService.setPosts(listOfPosts)
          console.log(listOfPosts);
      })).subscribe();
  }
  
}
