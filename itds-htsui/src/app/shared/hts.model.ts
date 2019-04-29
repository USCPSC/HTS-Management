export type HtsCode = {
  htsCode: string;
  htsCodeType: string;
  jurisdiction: string;
  htsDescription: string;
  targeted: string;
  startDate: string;
  endDate: string;
  notes: string;
  changeStatus: string;
  modified: string;
  sunset: string;
};

export type HtsTreeNode = {
  data: HtsCode;
  children: HtsTreeNode[];
};

export enum GlobalStateEnum {
  finalized = "FINALIZED_NO_WIP",
  upload = "ITC_UPLOAD_WIP",
  edit = "CPSC_CURRENT_WIP",
  transition = "IN_TRANSITION",
  unknown = "UNKNOWN",
}

export enum SourceEnum {
  currrent = "CURRENT",
  edit = "CPSC_CURRENT_WIP",
  upload_diffs = "DIFFS_FROM_UPLOAD",
  upload_all = "ITC_UPLOAD_WIP",
}

export enum PersistanceStatusEnum {
  finished = "SUCCEEDED",
  inprogress = "UNDERWAY",
}

export enum TransitionEnum {
  enableStart = "ENABLE_START",
  enableEnd = "ENABLE_END",
  uploadStart = "UPLOAD_START",
  uploadEnd = "UPLOAD_END",
  finalizeStart = "FINALIZE_START",
  finalizeEnd = "FINALIZE_END",
  revertStart = "REVERT_START",
  revertEnd = "REVERT_END",
  saveStart = "SAVE_START",
  saveEnd = "SAVE_END"
}

// { "username": "NO_TR_USR",
//   "globalState": "IN_TRANSITION",
//   "stateTransition": "UPLOAD_START",
//   "transitioningNow": "true",
//   "persistenceStatus": "UNDERWAY",
//   "persistenceProgressRemark": "Persistence is underway.",
//   "percentage": "93",
//   "progressInRecordsPersistedToScratchTable": "37000",
//   "progressGoalInRecords": "39567",
//   "progressLastCalculated": "2018-06-07 12:02:04", "timestamp": "2018-06-07 12:02:05"}

export type PersistanceState = {
  persistenceStatus: PersistanceStatusEnum;
  percentage: number;
};

export enum UploadErrorEnum {
  ok = "OK",
  parsing_line = "EXCEPTION_PARSING_LINE",
  parsing_indent = "EXCEPTION_PARSING_INDENT",
  scratch = "EXCEPTION_INSERTING_TO_SCRATCH_TABLE",
  reset = "SAW_RESET_FLAG",
  read = "EXCEPTION_READING_FILE",
  write = "EXCEPTION_WRITING_TO_FILE_TABLE"
}

export type HtsState = {
  username?: string;
  globalState?: GlobalStateEnum;
  stateTransition?: TransitionEnum;
  stateTransitionTimestamp?: string;
  transitioningNow?: boolean;
  persistenceStatus?: PersistanceStatusEnum;
  persistenceProgressRemark?: string;
  percentage?: number;
  lastUpload?: {
    timestamp: string;
    result: "success" | "failure";
    incidentCode: UploadErrorEnum;
    notes: string;
  }
};

export type HtsTree = {
  username: string;
  globalState: string;
  source: string;
  filter: string;
  searchterm: string;
  recordCount: number;
  htsView: HtsTreeNode[];
};


export enum FilterEnum {
  all = "no_filter",
  juris = "jurisdiction",
  target = "targeted"
}

export type Filter = {
  filter: FilterEnum;
  search?: string;
};

// export enum SourcesEnum {
//   current = "current",
//   all_upload = "all_from_upload",
//   diffs = "diffs_from_upload"
// }

export type HtsEditResponse = {
  editingCurrentIsEnabled: boolean;
  globalState: GlobalStateEnum;
};
// {"remark": "Reversion to previous finalization is complete. Work in progress has been discarded.", "recordCount": "39583"}
export type HtsRevertApplyResponse = {
  remark: string;
  recordCount: number;
};
export type Statistics = {
  refActiveTotal: string;
  refActiveTotalJurisdictionTrue: string;
  refActiveTotalTargetedTrue: string;
  refSunsetTotal?: string;
  distinctNonRetireeTensInScratch?: string;
  distinctNonRetireeJurisTensInScratch?: string;
  distinctNonRetireeTargetedTensInScratch?: string;
};

export class Hts {
  constructor(public htsCode: string,
              public htsCodeType: string,
              public jurisdiction: string,
              public htsDescription: string,
              public targeted: string,
              public startDate: string,
              public endDate: string,
              public notes: string) {
  }
}
