import * as React from 'react';

export interface ICheckedUncheckedElementProps {
    checked: boolean;
    children: any;
}

export const CheckedUncheckedElement = (props: ICheckedUncheckedElementProps) => <>
    <span
        className={props.checked
            ? 'glyphicon glyphicon-check'
            : 'glyphicon glyphicon-unchecked'}
    > {props.children}
    </span>
</>;