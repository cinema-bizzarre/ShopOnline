import { Product } from './product';

export class Cart {

    id: string;
    title: string;
    imageUrl: string;
    price: number;

    quantity: number;

    constructor(product: Product) {
        this.id = product.id;
        this.title = product.title;
        this.price = product.price;

        this.quantity = 1;
    }
}
