import { AbstractResource } from '../services/store-service';

export class Glee implements AbstractResource {

    id: number;
    name: string;
    date: Date;
    time: Date;
    text: string;
    value: number;
    userId: number;
    email: string;

    constructor(data: any = null) {
        if (data) {
        }
    }
}
