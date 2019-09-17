export interface IBaseSearchCondition {
}

type Id3Tag = 'ARTIST' | 'ALBUM';
type Operation = 'START_WITH' | 'CONTAINS';

export interface SearchCondition extends IBaseSearchCondition {
    id3Tag: Id3Tag;
    operation: Operation;
    value: string;
}

type PredicateType = 'AND' | 'OR' | 'NOT'
export interface PredicateCondition extends IBaseSearchCondition {
    type: PredicateType,
    conditions: IBaseSearchCondition
}


/**
 * {
  "conditions": [
    {
      "id3Tag": "ALBUM",
      "operation": "CONTAINS",
      "value": "asdf"
    },
    {
      "id3Tag": "ALBUM",
      "operation": "START_WITH",
      "value": "afdsafdsa"
    }
  ],
  "type": "OR"
}
 */