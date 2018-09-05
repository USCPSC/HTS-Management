export enum FilesStatus {
    STATUS_SUCCESS,
    STATUS_MAX_FILES_COUNT_EXCEED,
    STATUS_MAX_FILE_SIZE_EXCEED,
    STATUS_MAX_FILES_TOTAL_SIZE_EXCEED,
    STATUS_NOT_MATCH_EXTENSIONS
}

export interface FilesSelected {
  status: FilesStatus;
  files: File[];
}
