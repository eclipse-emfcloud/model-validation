# Model Validation
This frameworks eases the usage of the modelserver validation capabilities.

This model validation framework supports:
- Triggering a validation of a specific model
- Subscribing to validation result changes (when the model has changed)
- Retrieving a list of constraints that can be used for input validation
- Keeping a list of constraints that are turned off

# ChangeListener
The ValidationResultChangeListener can be used to update the respresentation of the ValidationResults everytime the ValidationResult for a specific model has changed.

# InputValidation
The framework contains a Map from ElementId and FeatureId (defined in the metamodel) to a EMFFacetConstraints. This Map returns all of the facets that are defined in the metamodel for the specific feature.

# Filtering Constraints
The framework also keeps a list of all the code/source (defined in the metamodel) pairs, that should be filtered. They can either be turned on/off or can be toggled.
