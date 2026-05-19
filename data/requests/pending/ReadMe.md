# Simulation Data for Demo

Simulate System until Mid of August.

## Timeline

- April 1, 2026: There are two Car Rental Booking Requests.
  - The first asks for a Premium Car with Duration 5 days (5 - 10 of April)
  - The second asks for a Premium Car with Duration 10 days (6 - 15 of April)
  - The second request will be serviced **with higher priority** 
    -  First Request Fails
    - Second Request is Processed Succesfully (referenceId: RF1774626096924588).

- April 5, 2026: Invalid Rental Booking Request. Fails.
  - The request asks for a rental that starts on April 1 (before the request timestamp on April 5).

- April 10, 2026: Thre is a Booking Request for Commercial Van with duration 1 year. Processed Succesfully (referenceId:RF1774626096924111)

- April 15, 2026: The Car Rental with referenceId RF1774626096924588 should be completed, but no Retnal Return Request exists

- April 16, 2026: Invalid Rental Return Request. Fails
  - Someone tries to return the active Car Rental (referenceId: RF1774626096924588) but uses the wrong VAT number (999999999 instead of 444555666).

- April 18, 2026: Rental Completion Request. Processed Succesfuly.
  - Customer returns car rented in rental with referenceId: RF1774626096924588
  - Rental is completed
  - Overdue charges are applied

- April 20, 2026: 
  - Customer Payment Request. Customer with vat:444555666 pays his wallet with amount 1040.00 Euros. Processed succesfully
  - Rental Cancelation Request. Fails (contract already completed)
    - Customer tries to cancel the reservation with referenceId RF1774626096924588.

- May 25, 2026: Fine Payment request for Plate: MNO-9012. Processed Succesfuly
  - Should be related with Car Rental RF1774626096924588 and charged to customer with vat 444555666.

- August 06, 2026: Rental Return Request for the Van Leasing (with referenceId:RF1774626096924111).
  - Leasing is completed
  - No refunds (it was charged month by month. Entire August is charged.)

- August 10, 2026: Car Rental Booking Request for an Economy car (duration 5 days, starts August 15). Processed Succesfuly.
  - Pre-booking is successful and a new CarRental is created (referenceId: RF1774626096924999).

- August 12, 2026: Rental Cancelation Request for the booking made on August 10 (referenceId: RF1774626096924999).
  - Booking is successfully cancelled before the start date, freeing up the booked Economy car.
