import {Component, OnInit} from '@angular/core';
import {FilesConfig, FilesSelected, FilesStatus} from "app/diff-dashboard/upload/declarations";
import {FilesService} from "app/diff-dashboard/upload/services";
import {DataService} from "app/shared/data.service";

@Component({
  selector: 'app-upload',
  templateUrl: './upload.component.html',
  styleUrls: ['./upload.component.scss']
})
export class UploadComponent implements OnInit {
  selectedFiles: FilesSelected;

  htsConfig: FilesConfig = {
    acceptExtensions: ['csv', 'txt'],
    maxFilesCount: 1,
    maxFileSize: 10485760, // size in bytes
    totalFilesSize: 10485760
  };

  constructor(private dataService: DataService,
              private filesService: FilesService) {
    this.filesService.addConfig(this.htsConfig, 'hts-config');
  }

  ngOnInit() {
  }

  formatFileSize(bytes) {
    if(bytes == 0) return '0 Bytes';
    var k = 1024,
      dm =  0,
      sizes = ['Bytes', 'KB', 'MB', 'GB', 'TB', 'PB', 'EB', 'ZB', 'YB'],
      i = Math.floor(Math.log(bytes) / Math.log(k));
    return parseFloat((bytes / Math.pow(k, i)).toFixed(dm)) + ' ' + sizes[i];
  }

  errorMessage(): string {
    if (this.selectedFiles && this.selectedFiles.status > 0) {
      switch (this.selectedFiles.status) {
        case FilesStatus.STATUS_NOT_MATCH_EXTENSIONS:
          return "File " + this.selectedFiles.files[0].name + " has incorrect type. Supported types: " + this.htsConfig.acceptExtensions;
        case FilesStatus.STATUS_MAX_FILES_COUNT_EXCEED:
          return "Too many files selected. Only one file allowed.";
        case FilesStatus.STATUS_MAX_FILE_SIZE_EXCEED:
          return "File " + this.selectedFiles.files[0].name + " is too big. Supported size: " + this.htsConfig.maxFileSize + " bytes";
      }
    }
    return '';
  }

  public filesSelect(selectedFiles: FilesSelected): void {
    this.selectedFiles = selectedFiles;
    console.log(this.selectedFiles);
    if (this.selectedFiles.status === FilesStatus.STATUS_SUCCESS) {
      this.dataService.postFile(this.selectedFiles.files[0]);
    }
  }

}
