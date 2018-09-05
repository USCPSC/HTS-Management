import { Injectable } from '@angular/core';
import {FilesConfig, FilesConfigDefault} from "app/diff-dashboard/upload/declarations";


@Injectable()
export class FilesService {

    private static readonly ERROR_MSG_PREFIX = 'ng4Files:';

    private configs: { [key: string]: FilesConfig } = {};

    private static throwError(
        msg: string,
        type: 'default' | 'range' | 'syntax' | 'reference' = 'default'
    ): never {
        const fullMsg = `${FilesService.ERROR_MSG_PREFIX} ${msg}`;

        switch (type) {
            case 'default':
                throw new Error(fullMsg);
            case 'range':
                throw new RangeError(fullMsg);
            case 'syntax':
                throw new SyntaxError(fullMsg);
            case 'reference':
                throw new ReferenceError(fullMsg);
        }
    }

    public addConfig(config: FilesConfig, configId = 'shared'): void {
        this.newConfigVerifyPipeline(config);
        this.configs[configId] = config;
    }

    public getConfig(configId = 'shared'): FilesConfig {
        if (configId === 'shared' && !this.configs['shared']) {
            this.configs['shared'] = <FilesConfig>{};
            this.setDefaultProperties(this.configs['shared']);
        }

        if (!this.configs[configId]) {
            FilesService.throwError(`Config '${configId}' is not found`, 'reference');
        }

        return this.configs[configId];
    }

    private newConfigVerifyPipeline(config): void {
        this.isUnique(config)
            .setDefaultProperties(config)
            .isFilesCountValid(config)
            .isAcceptExtensionsValid(config)
            .isFileSizeRangesValid(config)
            .transformAcceptExtensions(config);
    }

    private isUnique(config): FilesService {
        const isConfigExist = Object.keys(this.configs).find((key: string) => this.configs[key] === config);
        if (isConfigExist) {
            FilesService.throwError('Avoid add the same config more than once');
        }

        return this;
    }

    private setDefaultProperties(config: FilesConfig): FilesService {
        config.acceptExtensions = config.acceptExtensions || FilesConfigDefault.acceptExtensions;
        config.maxFileSize = config.maxFileSize || FilesConfigDefault.maxFileSize;
        config.totalFilesSize = config.totalFilesSize || FilesConfigDefault.totalFilesSize;
        config.maxFilesCount = config.maxFilesCount === 0 ?
            config.maxFilesCount :
            config.maxFilesCount || FilesConfigDefault.maxFilesCount;

        return this;
    }

    private isFilesCountValid(config): FilesService {
        if (config.maxFilesCount < 1) {
            const FILES_COUNT_MIN = 1;
            const FILES_COUNT_MAX = Infinity;

            FilesService.throwError(`maxFilesCount must be between ${FILES_COUNT_MIN} and ${FILES_COUNT_MAX}`, 'range');
        }

        return this;
    }

    private isAcceptExtensionsValid(config): FilesService {
        if (typeof config.acceptExtensions === 'string' && config.acceptExtensions !== '*') {
            FilesService.throwError(`acceptanceExtensions type must be "*" or string[]`, 'syntax');
        }

        return this;
    }

    private isFileSizeRangesValid(config): FilesService {
        if (config.maxFileSize > config.totalFilesSize) {
            FilesService.throwError('maxFileSize must be less than totalFilesSize', 'range');
        }

        return this;
    }

    private transformAcceptExtensions(config): FilesService {
        if (
            config.acceptExtensions === '*' ||
            config.acceptExtensions.indexOf('*') !== -1 ||
            Array.isArray(config.acceptExtensions) && config.acceptExtensions.length === 0
        ) {
            config.acceptExtensions = '*/*';
        } else {
            config.acceptExtensions = (config.acceptExtensions as string[])
                .map(extension => '.' + extension.toLowerCase()).join(', ');
        }

        return this;
    }

}
