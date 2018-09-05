import {
    Component,
    DoCheck,
    Input,
    Output,
    EventEmitter,
    ChangeDetectionStrategy,
    ChangeDetectorRef,
    HostListener
} from '@angular/core';
import {FilesSelected} from "app/diff-dashboard/upload/declarations";
import {FilesUtilsService} from "app/diff-dashboard/upload/services";


@Component({
    selector: 'files-drop', // tslint:disable-line
    templateUrl: './files-drop.component.html',
    changeDetection: ChangeDetectionStrategy.OnPush
})
export class FilesDropComponent implements DoCheck {

    @Input() private configId = 'shared';

    @Output() filesSelect: EventEmitter<FilesSelected> = new EventEmitter<FilesSelected>();

    @HostListener('dragenter', ['$event'])
    public onDragEnter(event: any) {
        this.preventEvent(event);
    }

    @HostListener('dragover', ['$event'])
    public onDragOver(event: any) {
        this.preventEvent(event);
    }

    @HostListener('drop', ['$event'])
    public onDrop(event: any) {
        this.preventEvent(event);

        if (!event.dataTransfer || !event.dataTransfer.files) {
            return;
        }

        this.dropFilesHandler(event.dataTransfer.files);
    }

    constructor(private changeDetector: ChangeDetectorRef,
                private FilesUtilsService: FilesUtilsService) {
    }

    ngDoCheck() {
        this.changeDetector.detectChanges();
    }

    private dropFilesHandler(files: FileList) {
        this.filesSelect.emit(
            this.FilesUtilsService.verifyFiles(files, this.configId)
        );
    }

    private preventEvent(event: any): void {
        event.stopPropagation();
        event.preventDefault();
    }

}
