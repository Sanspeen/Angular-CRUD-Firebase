import { Injectable } from '@angular/core';
import { Post } from './post/post.model';

@Injectable({ providedIn: 'root' })
export class PostService {
  listOfPosts: Post[] = [
    new Post(
      'Nature',
      'This is just a writting by Santi to test This Angular application and I have to write something about nature because it was the example',
      'https://static.educalingo.com/img/en/800/nature.jpg',
      'test@test.com',
      new Date()
    ),
    new Post(
      'Medellin',
      'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nam non purus nec metus vulputate consequat. Integer tristique posuere sem non pellentesque. Phasellus in imperdiet quam. Ut gravida eros at pulvinar.',
      'https://www.fenalcoantioquia.com/wp-content/uploads/2021/04/medellin.jpeg',
      'test@test.com',
      new Date()
    ),
    new Post(
      'Tomorrowland',
      'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nam non purus nec metus vulputate consequat. Integer tristique posuere sem non pellentesque. Phasellus in imperdiet quam. Ut gravida eros at pulvinar.',
      'https://nextlevelglobal.net/wp-content/uploads/elementor/thumbs/Tomorrowland-Festival-Foto-2020-p41xig4vjf9oyeul1g5gv4vtqmkaus9wd4s3ee9do0.jpg',
      'test@test.com',
      new Date()
    ),
    new Post(
      'Norway',
      'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nam non purus nec metus vulputate consequat. Integer tristique posuere sem non pellentesque. Phasellus in imperdiet quam. Ut gravida eros at pulvinar.',
      'https://www.fodors.com/wp-content/uploads/2020/01/UltimateNorway__HERO_iStock-1127199612.jpg',
      'test@test.com',
      new Date()
    ),
    new Post(
      'Medellin',
      'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nam non purus nec metus vulputate consequat. Integer tristique posuere sem non pellentesque. Phasellus in imperdiet quam. Ut gravida eros at pulvinar.',
      'https://www.fenalcoantioquia.com/wp-content/uploads/2021/04/medellin.jpeg',
      'test@test.com',
      new Date()
    ),
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

}
