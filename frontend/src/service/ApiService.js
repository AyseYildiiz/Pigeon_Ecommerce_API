import axios from "axios";

export default class ApiService {
    static BASE_URL = "http://localhost:2424";

    static getHeader() {
        const token = localStorage.getItem("token");
        return {
            "Content-Type": "application/json",
            Authorization: `Bearer ${token}`,
        };
    }

    //Auth and Login APIs
    static async register(registeration) {
        const response = await axios.post(
            `${this.BASE_URL}/auth/register`,
            registeration
        );
        return response.data;
    }

    static async login(loginDetails) {
        const response = await axios.post(
            `${this.BASE_URL}/auth/login`,
            loginDetails
        );
        return response.data;
    }

    static async getLoggedInUserInfo() {
        const response = await axios.get(`${this.BASE_URL}/user/my-info`, {
            headers: this.getHeader(),
        });
        return response.data;
    }

    //Product APIs
    static async addProduct(formData) {
        const response = await axios.post(
            `${this.BASE_URL}/product/create`,
            formData,
            {
                headers: {
                    ...this.getHeader(),
                    "Content-Type": "multipart/form-data",
                },
            }
        );
        return response.data;
    }

    static async updateProduct(productId, formData) {
        const response = await axios.put(
            `${this.BASE_URL}/product/update/${productId}`,
            formData,
            {
                headers: {
                    ...this.getHeader(),
                    "Content-Type": "multipart/form-data",
                },
            }
        );
        return response.data;
    }

    static async getAllProducts() {
        const response = await axios.get(`${this.BASE_URL}/product/get-all`);
        return response.data;
    }

    static async searchProducts(searchValue) {
        const response = await axios.get(`${this.BASE_URL}/product/search`, {
            params: {searchValue},
        });
        return response.data;
    }

    static async getProductsByCategory(categoryId) {
        const response = await axios.get(
            `${this.BASE_URL}/product/get-by-category-id/${categoryId}`
        );
        return response.data;
    }

    static async getProductsById(productId) {
        const response = await axios.get(
            `${this.BASE_URL}/product/get-by-product-id/${productId}`
        );
        return response.data;
    }

    static async deleteProduct(productId) {
        const response = await axios.get(
            `${this.BASE_URL}/product/delete/${productId}`,
            {headers: this.getHeader()}
        );
        return response.data;
    }

    //Category APIs
    static async createCategory(body) {
        const response = await axios.post(
            `${this.BASE_URL}/category/create`,
            body,
            {headers: this.getHeader()}
        );
        return response.data;
    }

    static async getAllCategories() {
        const response = await axios.get(`${this.BASE_URL}/category/get-all`);
        return response.data;
    }

    static async getCategoryById(categoryId) {
        const response = await axios.get(
            `${this.BASE_URL}/category/get-by-category-id/${categoryId}`
        );
        return response.data;
    }

    static async updateCategory(categoryId, body) {
        const response = await axios.put(
            `${this.BASE_URL}/category/update/${categoryId}`,
            body,
            {headers: this.getHeader()}
        );
        return response.data;
    }

    static async deleteCategory(categoryId) {
        const response = await axios.delete(
            `${this.BASE_URL}/category/delete/${categoryId}`,
            {headers: this.getHeader()}
        );
        return response.data;
    }

    //Order APIs
    static async createOrder(orderDetails) {
        const response = await axios.post(
            `${this.BASE_URL}/order/create`,
            orderDetails,
            {headers: this.getHeader()}
        );
        return response.data;
    }

    static async getAllOrders() {
        const response = await axios.get(`${this.BASE_URL}/order/filter`, {
            headers: this.getHeader(),
        });
        return response.data;
    }

    static async getOrderItemById(itemId) {
        const response = await axios.get(`${this.BASE_URL}/order/filter`, {
            headers: this.getHeader(),
            params: {itemId},
        });
        return response.data;
    }

    static async getOrderItemBystatus(status) {
        const response = await axios.get(`${this.BASE_URL}/order/filter`, {
            headers: this.getHeader(),
            params: {status},
        });
        return response.data;
    }

    static async updateOrderItemBystatus(orderItemId, status) {
        const response = await axios.get(
            `${this.BASE_URL}/order/update-item-status/${orderItemId}`,
            {
                headers: this.getHeader(),
                params: {status},
            }
        );
        return response.data;
    }

    //Address APIs
    static async saveAddress(addressDetails) {
        const response = await axios.post(
            `${this.BASE_URL}/user/save`,
            addressDetails,
            {headers: this.getHeader()}
        );
        return response.data;
    }

    static logout() {
        localStorage.removeItem("token");
        localStorage.removeItem("role");
    }

    static isAuthenticated() {
        const token = localStorage.getItem("token");
        return !!token;
    }

    static isAdmin() {
        const role = localStorage.getItem("role");
        return role === "ADMIN";
    }
}


