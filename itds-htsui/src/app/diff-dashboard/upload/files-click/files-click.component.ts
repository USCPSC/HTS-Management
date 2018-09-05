import {
  ChangeDetectionStrategy,
  ChangeDetectorRef,
  Component,
  DoCheck,
  EventEmitter,
  Input,
  OnInit,
  Output
} from '@angular/core';
import {FilesSelected} from "app/diff-dashboard/upload/declarations";
import {FilesService, FilesUtilsService} from "app/diff-dashboard/upload/services";


@Component({
  selector: 'files-click', // tslint:disable-line
  templateUrl: './files-click.component.html',
  styleUrls: ['./files-click.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class FilesClickComponent implements OnInit, DoCheck {

  @Input() configId = 'shared';

  @Output() filesSelect: EventEmitter<FilesSelected> = new EventEmitter<FilesSelected>();

  public maxFilesCount: number;
  public acceptExtensions: string;

  constructor(
    private changeDetector: ChangeDetectorRef,
    private FilesService: FilesService,
    private FilesUtilsService: FilesUtilsService
  ) {
  }

  ngDoCheck() {
    this.changeDetector.detectChanges();
  }

  ngOnInit() {
    const config = this.FilesService.getConfig(this.configId);

    this.maxFilesCount = config.maxFilesCount;
    this.acceptExtensions = <string>config.acceptExtensions;
  }

  public onChange(files: FileList): void {
    console.log('onChange called');
    if (!files.length) {
      return;
    }

    this.filesSelect.emit(
      this.FilesUtilsService.verifyFiles(files, this.configId)
    );
  }

}
