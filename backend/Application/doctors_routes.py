# import datetime
#
# from flask import request, Blueprint
#
# from Application.Model.Doctor import Doctors
# from Application.Model.Metadata import Metadata
# from Application.Model.Response import Response
# from Application.Utils.doctors_query_utils import paginate, get_total_of_pages
# from flask import jsonify
# from flask_cors import cross_origin
#
# doctorapi = Blueprint('doctorapi', __name__)
#
# @cross_origin()
# @doctorapi.route('/doctor/', methods=["GET"])
# def doctor():
#     if request.method == "GET":
#         query = request.args
#         doctors = paginate(Doctors.query, query)
#         response = Response(Metadata(get_total_of_pages(doctors)), doctors)
#         return jsonify(response.serialize())