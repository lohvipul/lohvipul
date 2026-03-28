$(function () {



    // User Register validation

    var $userRegister = $("#userRegister");



    $userRegister.validate({

        rules: {

            name: {

                required: true,

                lettersonly: true

            },

            email: {

                required: true,

                space: true,

                email: true

            },

            mobileNumber: {

                required: true,

                space: true,

                numericOnly: true,

                minlength: 10,

                maxlength: 12

            },

            password: {

                required: true,

                space: true,

                strongPassword: true

            },

            confirmpassword: {

                required: true,

                space: true,

                equalTo: '#pass'

            },

            address: {

                required: true,

                all: true

            },

            city: {

                required: true,

                space: true

            },

            state: {

                required: true

            },

            pincode: {

                required: true,

                space: true,

                numericOnly: true

            }

        },

        messages: {

            name: {

                required: 'name required',

                lettersonly: 'invalid name'

            },

            email: {

                required: 'email name must be required',

                space: 'space not allowed',

                email: 'Invalid email'

            },

            mobileNumber: {

                required: 'mob no must be required',

                space: 'space not allowed',

                numericOnly: 'invalid mob no',

                minlength: 'min 10 digit',

                maxlength: 'max 12 digit'

            },

            password: {

                required: 'password must be required',

                space: 'space not allowed',

                strongPassword: 'Password must be 8-15 chars, include upper, lower, number & special char'

            },

            confirmpassword: {

                required: 'confirm password must be required',

                space: 'space not allowed',

                equalTo: 'password mismatch'

            },

            address: {

                required: 'address must be required',

                all: 'invalid'

            },

            city: {

                required: 'city must be required',

                space: 'space not allowed'

            },

            state: {

                required: 'state must be required',

                space: 'space not allowed'

            },

            pincode: {

                required: 'pincode must be required',

                space: 'space not allowed',

                numericOnly: 'invalid pincode'

            }

           

        }

    })





    // Orders Validation

    var $orders = $("#orders");



    $orders.validate({

        rules: {

            firstName: {

                required: true,

                lettersonly: true

            },

            lastName: {

                required: true,

                lettersonly: true

            },

            email: {

                required: true,

                space: true,

                email: true

            },

            mobileNo: {

                required: true,

                space: true,

                numericOnly: true,

                minlength: 10,

                maxlength: 12

            },

            address: {

                required: true,

                all: true

            },

            city: {

                required: true,

                space: true

            },

            state: {

                required: true,

            },

            pincode: {

                required: true,

                space: true,

                numericOnly: true

            },

            paymentType: {

                required: true

            }

        },

        messages: {

            firstName: {

                required: 'first required',

                lettersonly: 'invalid name'

            },

            lastName: {

                required: 'last name required',

                lettersonly: 'invalid name'

            },

            email: {

                required: 'email name must be required',

                space: 'space not allowed',

                email: 'Invalid email'

            },

            mobileNo: {

                required: 'mob no must be required',

                space: 'space not allowed',

                numericOnly: 'invalid mob no',

                minlength: 'min 10 digit',

                maxlength: 'max 12 digit'

            },

            address: {

                required: 'address must be required',

                all: 'invalid'

            },

            city: {

                required: 'city must be required',

                space: 'space not allowed'

            },

            state: {

                required: 'state must be required',

                space: 'space not allowed'

            },

            pincode: {

                required: 'pincode must be required',

                space: 'space not allowed',

                numericOnly: 'invalid pincode'

            },

            paymentType: {

                required: 'select payment type'

            }

        }

    })





    // Reset Password Validation

    var $resetPassword = $("#resetPassword");



    $resetPassword.validate({

        rules: {

            password: {

                required: true,

                space: true,

                strongPassword: true

            },

            confirmPassword: {

                required: true,

                space: true,

                equalTo: '#pass'

            }

        },

        messages: {

            password: {

                required: 'password must be required',

                space: 'space not allowed',

                strongPassword: 'Password must be 8-15 chars, include upper, lower, number & special char'

            },

            confirmPassword: {

                required: 'confirm password must be required',

                space: 'space not allowed',

                equalTo: 'password mismatch'

            }

        }

    })



})



// ================== Add Product Validation ==================

var $addProduct = $("#addProduct");



$addProduct.validate({

    rules: {

        title: {

            required: true,

            all: true

        },

        description: {

            required: true,

            minlength: 10

        },

        category: {

            required: true

        },

        price: {

            required: true,

            numericOnly: true,

            min: 1

        },

        isActive: {

            required: true

        },

        stock: {

            required: true,

            numericOnly: true,

            min: 1

        },

        file: {

            required: true,

            extension: "jpg|jpeg|png"

        }

    },

    messages: {

        title: {

            required: "Product title is required",

            all: "Invalid title"

        },

        description: {

            required: "Description is required",

            minlength: "Description must be at least 10 characters"

        },

        category: {

            required: "Please select a category"

        },

        price: {

            required: "Price is required",

            numericOnly: "Price must be numeric",

            min: "Price must be at least 1"

        },

        isActive: {

            required: "Please select product status"

        },

        stock: {

            required: "Stock is required",

            numericOnly: "Stock must be numeric",

            min: "Stock must be at least 1"

        },

        file: {

            required: "Product image is required",

            extension: "Only jpg, jpeg, or png files allowed"

        }

    }

})

// add category 
var $editCategory = $("#editCategory");

$editCategory.validate({
    rules: {
        name: {
            required: true,
            
        },
        isActive: {
            required: true
        },
        file: {
            required: true,
            extension: "jpg|jpeg|png|gif"
        }
    },
    messages: {
        name: {
            required: "Please enter category name"
           
        },
        isActive: {
            required: "Please select category status (Active/Inactive)"
        },
        file: {
            required: "Please upload an image",
            extension: "Only image files (jpg, jpeg, png, gif) are allowed"
        }
		 },
		    errorElement: "span",
		    errorClass: "text-danger",
		    highlight: function (element) {
		        $(element).addClass("is-invalid");
		    },
		    unhighlight: function (element) {
		        $(element).removeClass("is-invalid");
		    }
		});



// ================== Edit Product Validation ==================

var $editProduct = $("#editProduct");



$editProduct.validate({

    rules: {

        title: {

            required: true,

            all: true

        },

        description: {

            required: true,

            minlength: 10

        },

        category: {

            required: true

        },

        price: {

            required: true,

            numericOnly: true,

            min: 1

        },

		discount: {

		    numericOnly: true,

		    min: 0,

		    max: 99

		},



        isActive: {

            required: true

        },

		stock: {

		    required: true,

		    numericOnly: true,

		    onlyZero: true

		},

        file: {

            extension: "jpg|jpeg|png"

        }

    },

    messages: {

        title: {

            required: "Product title is required",

            all: "Invalid title"

        },

        description: {

            required: "Description is required",

            minlength: "Description must be at least 10 characters"

        },

        category: {

            required: "Please select a category"

        },

        price: {

            required: "Price is required",

            numericOnly: "Price must be numeric",

            min: "Price must be at least 1"

        },

		discount: {

		    numericOnly: "Discount must be numeric",

		    min: "Discount cannot be negative",

		    max: "Discount cannot be more than 99"

		},

        isActive: {

            required: "Please select product status"

        },

		stock: {

		    required: "Stock is required",

		    numericOnly: "Stock must be numeric",

		    onlyZero: "Stock must be 0"

		},

        file: {

            extension: "Only jpg, jpeg, or png files allowed"

        }

    }

});







// Custom validation methods

jQuery.validator.addMethod('lettersonly', function (value, element) {

    return /^[^-\s][a-zA-Z_\s-]+$/.test(value);

});



jQuery.validator.addMethod('space', function (value, element) {

    return /^[^-\s]+$/.test(value);

});



jQuery.validator.addMethod('all', function (value, element) {

    return /^[^-\s][a-zA-Z0-9_,.\s-]+$/.test(value);

});



jQuery.validator.addMethod('numericOnly', function (value, element) {

    return /^[0-9]+$/.test(value);

});

jQuery.validator.addMethod("onlyZero", function (value, element) {

    return this.optional(element) || value === "0";

}, "Stock must be 0");



// ✅ Strong password validation

jQuery.validator.addMethod("strongPassword", function (value, element) {

    return this.optional(element)

        || /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&#])[A-Za-z\d@$!%*?&#]{8,15}$/.test(value);

}, "Password must be 8-15 characters long and include lowercase, uppercase, number, and special character.");

