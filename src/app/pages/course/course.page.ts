import { Component, inject, OnInit } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { CourseService } from 'src/app/services/course.service';

@Component({
  selector: 'app-course',
  templateUrl: './course.page.html',
  styleUrls: ['./course.page.scss'],
})
export class CoursePage implements OnInit {

  courseForm: FormGroup;
  selectedImage: File | null = null;
  selectedPdf: File | null = null;
  selectedVideos: File[] = [];

  private fb = inject(FormBuilder);
  private courseService= inject(CourseService);

  constructor() {}

  ngOnInit(): void {
    this.courseForm = this.fb.group({
      name: [''],
      description: [''],
      image: [null],
      pdf: [null],
      videos: [null]
    });
  }

  onFileChange(event: any, type: string): void {
    if (type === 'image') {
      this.selectedImage = event.target.files[0];
    } else if (type === 'pdf') {
      this.selectedPdf = event.target.files[0];
    } else if (type === 'videos') {
      this.selectedVideos = Array.from(event.target.files);
    }
  }

  onSubmit(): void {
    const formData = new FormData();
    formData.append('course', JSON.stringify(this.courseForm.value));

    if (this.selectedImage) {
      formData.append('image', this.selectedImage);
    }

    if (this.selectedPdf) {
      formData.append('pdf', this.selectedPdf);
    }

    this.selectedVideos.forEach((video, index) => {
      formData.append('videos', video);
    });

    console.log("formData",formData);
    this.courseService.createCourse(formData).subscribe(response => {
      console.log('Course created successfully', response);
    });
  }

}
