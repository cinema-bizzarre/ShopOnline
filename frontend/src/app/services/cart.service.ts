import {Injectable} from '@angular/core';
import {Cart} from '../common/cart';
import {Subject} from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class CartService {

  cart: Cart[] = [];

  totalPrice: Subject<number> = new Subject<number>();
  totalQuantity: Subject<number> = new Subject<number>();

  constructor() {
  }

  addToCart = (theCart: Cart): void => {

    let alreadyExistsInCart = false;
    let existingCart: Cart;

    if (this.cart.length > 0) {

      existingCart = this.cart.find(tempCart => tempCart.id === theCart.id);

      alreadyExistsInCart = (existingCart !== undefined);
    }

    if (alreadyExistsInCart) {
       existingCart.quantity++;
    } else {
      this.cart.push(theCart);
    }

    this.computeCartTotals();
  }

  computeCartTotals = (): void => {

    let totalPriceValue = 0;
    let totalQuantityValue = 0;

    for (const currentCart of this.cart) {
      totalPriceValue += currentCart.quantity * currentCart.price;
      totalQuantityValue += currentCart.quantity;
    }

    this.totalPrice.next(totalPriceValue);
    this.totalQuantity.next(totalQuantityValue);

    this.logCartData(totalPriceValue, totalQuantityValue);
  }

  logCartData = (totalPriceValue: number, totalQuantityValue: number): void => {

    console.log('Contents of the cart');
    for (const tempCart of this.cart) {
      const subTotalPrice = tempCart.quantity * tempCart.price;
      console.log(`name: ${tempCart.name}, quantity=${tempCart.quantity}, unitPrice=${tempCart.price},
      subTotalPrice=${subTotalPrice}`);
    }

    console.log(`totalPrice: ${totalPriceValue.toFixed(2)}, totalQuantity: ${totalQuantityValue}`);
    console.log('----');
  }

  decrementQuantity = (theCart: Cart): void => {

    theCart.quantity--;

    if (theCart.quantity === 0) {
      this.remove(theCart);
    } else {
      this.computeCartTotals();
    }
  }

  remove = (theCart: Cart): void => {

    const itemIndex = this.cart.findIndex(tempCart=> tempCart.id === theCart.id);

    if (itemIndex > -1) {
      this.cart.splice(itemIndex, 1);

      this.computeCartTotals();
    }
  }
}
