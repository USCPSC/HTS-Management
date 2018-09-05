export interface FilesConfig {
  acceptExtensions?: string[] | string;
  maxFilesCount?: number;
  maxFileSize?: number;
  totalFilesSize?: number;
}

export const FilesConfigDefault: FilesConfig = {
  acceptExtensions: '*',
  maxFilesCount: Infinity,
  maxFileSize: Infinity,
  totalFilesSize: Infinity
};
