import { Injectable } from '@angular/core';
import {FilesSelected, FilesStatus} from "app/diff-dashboard/upload/declarations";
import {FilesService} from "app/diff-dashboard/upload/services/files.service";


@Injectable()
export class FilesUtilsService {

    private static getRegExp(extensions: string): RegExp {
        return new RegExp(`(.*?)\.(${extensions})$`);
    }

    constructor(private FilesService: FilesService) {
    }

    public verifyFiles(files: FileList, configId = 'shared'): FilesSelected {
        const filesArray = Array.from(files);

        const config = this.FilesService.getConfig(configId);
        const maxFilesCount = config.maxFilesCount;
        const totalFilesSize = config.totalFilesSize;
        const acceptExtensions = config.acceptExtensions;

        if (filesArray.length > maxFilesCount) {
            return <FilesSelected> {
                status: FilesStatus.STATUS_MAX_FILES_COUNT_EXCEED,
                files: filesArray
            };
        }

        const filesWithExceedSize = filesArray.filter((file: File) => file.size > config.maxFileSize);
        if (filesWithExceedSize.length) {
            return <FilesSelected> {
                status: FilesStatus.STATUS_MAX_FILE_SIZE_EXCEED,
                files: filesWithExceedSize
            };
        }

        let filesSize = 0;
        filesArray.forEach((file: File) => filesSize += file.size);
        if (filesSize > totalFilesSize) {
            return <FilesSelected> {
                status: FilesStatus.STATUS_MAX_FILES_TOTAL_SIZE_EXCEED,
                files: filesArray
            };
        }

        const filesNotMatchExtensions = filesArray.filter((file: File) => {
            const extensionsList = (acceptExtensions as string)
                .split(', ')
                .map(extension => extension.slice(1))
                .join('|');

            const regexp = FilesUtilsService.getRegExp(extensionsList);

            return !regexp.test(file.name);
        });

        if (filesNotMatchExtensions.length) {
            return <FilesSelected> {
                status: FilesStatus.STATUS_NOT_MATCH_EXTENSIONS,
                files: filesNotMatchExtensions
            };
        }

        return <FilesSelected> {
            status: FilesStatus.STATUS_SUCCESS,
            files: filesArray
        };
    }

}
